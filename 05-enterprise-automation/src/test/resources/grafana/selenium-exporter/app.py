import http.server
import json
import os
import socketserver
import sys
from urllib.parse import urlparse
import requests

SELENIUM_HOST = os.environ.get("SELENIUM_HOST", "http://selenium-hub:4444")
PORT = 9278

HELP_TEXT = """
# HELP selenium_grid_up Grid status: 1 if ready, 0 otherwise
# TYPE selenium_grid_up gauge
selenium_grid_up {up}
# HELP selenium_grid_nodes Number of nodes registered
# TYPE selenium_grid_nodes gauge
selenium_grid_nodes {nodes}
# HELP selenium_grid_slots_total Total slots across all nodes
# TYPE selenium_grid_slots_total gauge
selenium_grid_slots_total {slots_total}
# HELP selenium_grid_slots_used Slots currently occupied (session != null)
# TYPE selenium_grid_slots_used gauge
selenium_grid_slots_used {slots_used}
# HELP selenium_grid_slots_free Slots currently free
# TYPE selenium_grid_slots_free gauge
selenium_grid_slots_free {slots_free}
# HELP selenium_grid_sessions_active Active sessions across grid (alias of slots_used)
# TYPE selenium_grid_sessions_active gauge
selenium_grid_sessions_active {sessions}
"""

class Handler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        if urlparse(self.path).path != "/metrics":
            self.send_response(404)
            self.end_headers()
            return
        try:
            r = requests.get(f"{SELENIUM_HOST}/status", timeout=5)
            data = r.json()
            value = data.get("value", {})
            nodes_data = value.get("nodes", []) or []
            ready = int(bool(value.get("ready", False)))

            slots_total = slots_used = 0
            node_metrics = []
            for n in nodes_data:
                slots = n.get("slots", []) or []
                node_id = n.get("id", "")
                uri = n.get("uri", "")
                max_sessions = int(n.get("maxSessions", 0) or 0)
                used = sum(1 for s in slots if s.get("session"))
                total = len(slots)
                free = total - used
                slots_total += total
                slots_used += used
                node_up = 1 if n.get("availability", "").upper() == "UP" else 0
                node_metrics.append(
                    f"selenium_node_up{{node_id=\"{node_id}\",uri=\"{uri}\"}} {node_up}\n"
                    f"selenium_node_slots_total{{node_id=\"{node_id}\",uri=\"{uri}\"}} {total}\n"
                    f"selenium_node_slots_used{{node_id=\"{node_id}\",uri=\"{uri}\"}} {used}\n"
                    f"selenium_node_slots_free{{node_id=\"{node_id}\",uri=\"{uri}\"}} {free}\n"
                    f"selenium_node_max_sessions{{node_id=\"{node_id}\",uri=\"{uri}\"}} {max_sessions}\n"
                )

            slots_free = slots_total - slots_used
            body = HELP_TEXT.format(
                up=ready,
                nodes=len(nodes_data),
                slots_total=slots_total,
                slots_used=slots_used,
                slots_free=slots_free,
                sessions=slots_used,
            )
            # ensure a newline before node metrics so the first line is not glued to sessions line
            body += "\n" + "\n".join(node_metrics) + "\n"

            self.send_response(200)
            self.send_header("Content-Type", "text/plain; version=0.0.4; charset=utf-8")
            self.end_headers()
            self.wfile.write(body.encode("utf-8"))
        except Exception as e:
            err = f"# scrape_error {e}\n"
            self.send_response(500)
            self.send_header("Content-Type", "text/plain")
            self.end_headers()
            self.wfile.write(err.encode("utf-8"))

def main():
    with socketserver.TCPServer(("0.0.0.0", PORT), Handler) as httpd:
        print(f"serving metrics on :{PORT}, SELENIUM_HOST={SELENIUM_HOST}")
        httpd.serve_forever()

if __name__ == "__main__":
    sys.exit(main())
