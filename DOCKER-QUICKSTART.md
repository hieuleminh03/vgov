# 🚀 V-GOV Backend - Docker Quick Start

## One-Command Setup

```bash
# Start everything
./docker-dev.sh start

# Or using docker-compose directly
docker-compose up -d
```

## Access Points

After starting (wait 2-3 minutes for full startup):

| Service | URL | Credentials |
|---------|-----|-------------|
| 🔗 **Backend API** | http://localhost:8080 | - |
| 🏥 **Health Check** | http://localhost:8080/actuator/health | - |
| 🗄️ **Database Admin** | http://localhost:8081 | System: PostgreSQL, Server: postgres, User: vgov_user, Password: vgov_password |
| 🗃️ **MinIO Console** | http://localhost:9001 | User: minioadmin, Password: minioadmin |

## Essential Commands

```bash
# Development workflow
./docker-dev.sh start          # Start all services
./docker-dev.sh logs           # View all logs
./docker-dev.sh logs vgov-backend  # View app logs only
./docker-dev.sh rebuild        # Rebuild after code changes
./docker-dev.sh stop           # Stop all services

# Database operations
./docker-dev.sh db             # Access database shell
./docker-dev.sh reset-db       # Reset database (destroys data!)

# System operations
./docker-dev.sh status         # Check service status
./docker-dev.sh cleanup        # Clean up Docker resources
```

## Project Structure

```
vgov/
├── docker-compose.yml    # Multi-service orchestration
├── Dockerfile           # Application container
├── .env                # Environment variables (development)
├── docker-dev.sh       # Convenience script
└── README-Docker.md    # Detailed documentation
```

## Development Workflow

1. **Make code changes**
2. **Rebuild and restart:**
   ```bash
   ./docker-dev.sh rebuild
   ```
3. **View logs:**
   ```bash
   ./docker-dev.sh logs vgov-backend
   ```

## API Testing

```bash
# Health check
curl http://localhost:8080/actuator/health

# Get system version
curl http://localhost:8080/api/system/version
```

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Services won't start | `./docker-dev.sh status` to check health |
| Port conflicts | Edit `.env` file to change ports |
| Database issues | `./docker-dev.sh reset-db` (destroys data!) |
| Application errors | `./docker-dev.sh logs vgov-backend` |
| General cleanup | `./docker-dev.sh cleanup` |

## Environment Configuration

Edit `.env` file to customize:
- Database credentials
- Application ports
- JWT secrets
- MinIO settings
- File upload limits

## Notes

⚠️ **Development Environment Only**
- Default passwords are insecure
- Debug logging is enabled
- No HTTPS/TLS configured

For production deployment, see detailed documentation in `README-Docker.md`.

---

**Need help?** Check `README-Docker.md` for comprehensive documentation.
