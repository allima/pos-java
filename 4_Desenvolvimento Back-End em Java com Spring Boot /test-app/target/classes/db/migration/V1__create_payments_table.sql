CREATE TABLE payments (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    pagador_id UUID NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    moeda VARCHAR(3) NOT NULL,
    method VARCHAR(20) NOT NULL,
    descricao VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    attachment_path VARCHAR(500)
);

CREATE INDEX idx_payment_idempotency ON payments(idempotency_key);
CREATE INDEX idx_payment_status ON payments(status);
CREATE INDEX idx_payment_created_at ON payments(created_at);
CREATE INDEX idx_payment_method ON payments(method);