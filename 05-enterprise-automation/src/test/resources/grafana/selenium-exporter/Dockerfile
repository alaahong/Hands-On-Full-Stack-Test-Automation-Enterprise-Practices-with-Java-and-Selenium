# Minimal Selenium Grid exporter (Prometheus text) built locally
FROM python:3.11-slim

WORKDIR /app
COPY app.py ./
RUN pip install --no-cache-dir requests

EXPOSE 9278
CMD ["python", "app.py"]

