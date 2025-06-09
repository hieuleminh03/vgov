# V-GOV Backend - Docker Development Setup

## Prerequisites

- Docker Desktop installed and running
- Docker Compose V2 installed
- WSL2 configured (for Windows users)

## Quick Start

1. **Navigate to the project directory:**
   ```bash
   cd vgov
   ```

2. **Start the development environment:**
   ```bash
   docker-compose up -d
   ```

3. **Wait for all services to be healthy (approximately 2-3 minutes):**
   ```bash
   docker-compose ps
   ```

4. **Access the services:**
   - **Backend API**: http://localhost:8080
   - **API Health Check**: http://localhost:8080/actuator/health
   - **Database Admin (Adminer)**: http://localhost:8081
   - **MinIO Console**: http://localhost:9001

## Service Details

### 🚀 V-GOV Backend Application
- **URL**: http://localhost:8080
- **Container**: `vgov-backend`
- **Health Check**: http://localhost:8080/actuator/health

### 🗄️ PostgreSQL Database
- **Host**: localhost:5432
- **Database**: vgov
- **Username**: vgov_user
- **Password**: vgov_password
- **Container**: `vgov-postgres`

### 🗃️ MinIO Object Storage
- **API**: http://localhost:9000
- **Console**: http://localhost:9001
- **Username**: minioadmin
- **Password**: minioadmin
- **Bucket**: vgov-files
- **Container**: `vgov-minio`

### 🛠️ Adminer (Database Management)
- **URL**: http://localhost:8081
- **System**: PostgreSQL
- **Server**: postgres
- **Username**: vgov_user
- **Password**: vgov_password
- **Database**: vgov

## Environment Configuration

### Environment Variables (.env file)
The `.env` file contains all configuration variables:

```bash
# Database
DB_NAME=vgov
DB_USERNAME=vgov_user
DB_PASSWORD=vgov_password
DB_PORT=5432

# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# Security
JWT_SECRET=your-very-long-secret-key-for-jwt-token-signing-in-development-environment-please-change-in-production

# MinIO
MINIO_PORT=9000
MINIO_CONSOLE_PORT=9001
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
MINIO_BUCKET_NAME=vgov-files

# File Upload
MAX_FILE_SIZE=10MB
MAX_REQUEST_SIZE=10MB

# Logging
LOG_LEVEL=INFO
```

### Custom Configuration
To override default settings, create a `.env.local` file:
```bash
cp .env .env.local
# Edit .env.local with your custom values
```

## Docker Commands

### Basic Operations
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f vgov-backend

# Restart a service
docker-compose restart vgov-backend

# Check service status
docker-compose ps
```

### Development Commands
```bash
# Rebuild application image (after code changes)
docker-compose build vgov-backend

# Rebuild and restart application
docker-compose up -d --build vgov-backend

# Execute command in running container
docker-compose exec vgov-backend bash

# View real-time logs
docker-compose logs -f --tail=100 vgov-backend
```

### Database Operations
```bash
# Access PostgreSQL shell
docker-compose exec postgres psql -U vgov_user -d vgov

# Backup database
docker-compose exec postgres pg_dump -U vgov_user vgov > backup.sql

# Restore database
docker-compose exec -T postgres psql -U vgov_user -d vgov < backup.sql

# Reset database (warning: destroys all data)
docker-compose down -v
docker-compose up -d
```

### MinIO Operations
```bash
# Access MinIO client
docker-compose exec minio mc --help

# List buckets
docker-compose exec minio mc ls myminio

# Create bucket manually
docker-compose exec minio mc mb myminio/new-bucket
```

## Troubleshooting

### Application Won't Start
1. **Check if all services are healthy:**
   ```bash
   docker-compose ps
   ```

2. **View application logs:**
   ```bash
   docker-compose logs vgov-backend
   ```

3. **Common issues:**
   - Database not ready: Wait for postgres health check to pass
   - Port conflicts: Change ports in `.env` file
   - Memory issues: Increase Docker memory allocation

### Database Connection Issues
1. **Verify PostgreSQL is running:**
   ```bash
   docker-compose logs postgres
   ```

2. **Check database connectivity:**
   ```bash
   docker-compose exec vgov-backend curl -f postgres:5432
   ```

3. **Reset database if corrupted:**
   ```bash
   docker-compose down -v
   docker volume rm vgov_postgres_data
   docker-compose up -d
   ```

### MinIO Access Issues
1. **Check MinIO status:**
   ```bash
   docker-compose logs minio
   ```

2. **Verify bucket creation:**
   ```bash
   docker-compose logs minio-client
   ```

3. **Manually create bucket:**
   ```bash
   docker-compose exec minio mc mb myminio/vgov-files --ignore-existing
   ```

### Performance Issues
1. **Monitor resource usage:**
   ```bash
   docker stats
   ```

2. **Check available disk space:**
   ```bash
   docker system df
   ```

3. **Clean up unused resources:**
   ```bash
   docker system prune -f
   ```

## Development Workflow

### Code Changes
1. **Make changes to Java code**
2. **Rebuild and restart application:**
   ```bash
   docker-compose up -d --build vgov-backend
   ```

### Database Schema Changes
1. **Update SQL files in `src/main/resources/sql/`**
2. **Restart database (if using init scripts):**
   ```bash
   docker-compose down postgres
   docker-compose up -d postgres
   ```

### Environment Changes
1. **Update `.env` file**
2. **Restart affected services:**
   ```bash
   docker-compose down
   docker-compose up -d
   ```

## API Testing

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Sample API Calls
```bash
# Login (after application starts and initial data is loaded)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@vgov.com","password":"admin123"}'

# Get system info
curl http://localhost:8080/api/system/version
```

## Data Persistence

### Volumes
- **postgres_data**: PostgreSQL database files
- **minio_data**: MinIO object storage files
- **app_uploads**: Application uploaded files

### Backup Strategy
```bash
# Backup all volumes
docker-compose down
docker run --rm -v vgov_postgres_data:/data -v $(pwd):/backup alpine tar czf /backup/postgres-backup.tar.gz /data
docker run --rm -v vgov_minio_data:/data -v $(pwd):/backup alpine tar czf /backup/minio-backup.tar.gz /data
docker-compose up -d
```

## Security Notes

⚠️ **Development Environment Only**
- Default passwords are used for convenience
- JWT secret is not secure
- MinIO buckets are publicly accessible
- Debug logging is enabled

🔒 **For Production:**
- Change all default passwords
- Use strong JWT secrets
- Configure proper MinIO permissions
- Enable HTTPS/TLS
- Review logging levels
- Use proper secrets management

## Port Configuration

Default ports used:
- **8080**: V-GOV Backend API
- **5432**: PostgreSQL Database
- **9000**: MinIO API
- **9001**: MinIO Console
- **8081**: Adminer Database UI

To change ports, modify the `.env` file and restart services.

---

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review application logs: `docker-compose logs vgov-backend`
3. Verify service health: `docker-compose ps`
4. Consult the main project documentation

Happy coding! 🚀
