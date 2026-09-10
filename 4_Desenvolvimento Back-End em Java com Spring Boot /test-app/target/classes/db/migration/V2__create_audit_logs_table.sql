CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    usuario VARCHAR(255) NOT NULL,
    acao VARCHAR(255) NOT NULL,
    recurso VARCHAR(500) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    resultado TEXT,
    latencia_ms BIGINT,
    metadata TEXT
);

CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_audit_user ON audit_logs(usuario);
CREATE INDEX idx_audit_resource ON audit_logs(recurso);