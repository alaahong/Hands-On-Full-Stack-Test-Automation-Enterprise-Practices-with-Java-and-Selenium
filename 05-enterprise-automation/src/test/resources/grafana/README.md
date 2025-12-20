# Grafana 11 + Loki/Promtail + Tempo + Selenium Grid（hub/node）一键可观测性

这个目录提供一个最小可运行的 Docker Compose 方案，覆盖可观测性的三大支柱：

- **Logs**：Promtail 采集容器 stdout/stderr → Loki 存储 → Grafana Explore 查询
- **Metrics**：Prometheus 抓取组件 `/metrics`（含 Selenium hub/node）→ Grafana 展示
- **Traces**：Tempo 接收 **OTLP**（gRPC/HTTP）→ Grafana Explore 查询与 Trace↔Log 关联

> 重点：所有组件在同一 Docker 网络互通，同时开放关键端口到宿主机，支持外部输入。

## 目录结构

- `docker-compose.yml`
- `loki/config.yaml`
- `promtail/config.yaml`
- `tempo/config.yaml`
- `prometheus/prometheus.yml`
- `grafana/provisioning/datasources/datasources.yaml`

## 对外端口（默认）

- Grafana：`http://localhost:3000`（admin/admin）
- Selenium Grid：`http://localhost:4444`（UI/WebDriver/metrics）
- Loki：`http://localhost:3100`（Loki API；外部可 push：`/loki/api/v1/push`）
- Tempo：
  - OTLP gRPC：`localhost:4317`
  - OTLP HTTP：`localhost:4318`
  - 查询：`http://localhost:3200`
- Prometheus：`http://localhost:9090`

## 快速启动

在本目录下启动：

```powershell
cd D:\code\selenium_sample\grafana
docker compose up -d
```

查看状态：

```powershell
docker compose ps
```

## 验证互通（建议）

### 1) 日志（Log）

打开 Grafana → Explore → 选择 Loki。

查询示例：

- Hub 日志：`{job="selenium-hub"}`
- Node 日志：`{job="selenium-node"}`

### 2) 指标（Metric）

打开 Grafana → Explore → 选择 Prometheus。

查询示例：

- Selenium 相关指标（按实际暴露为准）：`selenium_.*` 或 `jvm_.*`

也可以直接访问：

- `http://localhost:4444/metrics`
- `http://localhost:5555/metrics`

### 3) 链路（Trace）

Tempo 暴露 OTLP 入口，可从外部应用直接上报。

一个最简单的 smoke：用 OTel demo/SDK（任意语言）把 trace 发到：

- `OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318`

然后在 Grafana → Explore → Tempo 搜索 trace。

## Selenium 的可观测性接入点

- **Logs**：容器 stdout/stderr（已由 Promtail 自动采集）
- **Metrics**：`/metrics`（hub:4444, node:5555，Prometheus 已抓取）
- **Traces**：
  - Selenium 本身是否直接发 OTLP 取决于你启用的 tracing 配置与版本。
  - 当前 compose 先把 Tempo 的 OTLP 入口开放出来，方便你后续把测试脚本/应用/Collector 发到 Tempo。

## 常见问题

### Promtail 采集不到日志？

- 确认 Docker Desktop 允许挂载 `/var/run/docker.sock`（Linux 容器模式下通常 OK）。
- 你也可以放宽 `promtail/config.yaml` 里的 `relabel_configs`，采集更多容器再慢慢过滤。

### 想把 traces 和 logs 更精确关联？

需要你的应用日志里带 `trace_id`（或 traceId/traceid）。
Grafana 已在 Loki datasource 里配置了 derivedFields，会自动从日志行中提取并跳转关联。


