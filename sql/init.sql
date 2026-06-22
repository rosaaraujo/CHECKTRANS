CREATE TABLE IF NOT EXISTS checklists (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    transport_plate VARCHAR(15) NOT NULL,
    transport_type VARCHAR(20) NOT NULL,
    inspector_name VARCHAR(100) NOT NULL,
    check_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    observations TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS checklist_items (
    id BIGSERIAL PRIMARY KEY,
    checklist_id BIGINT NOT NULL REFERENCES checklists(id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,
    is_pass BOOLEAN,
    observations TEXT,
    item_order INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_checklists_status ON checklists(status);
CREATE INDEX IF NOT EXISTS idx_checklists_inspector ON checklists(inspector_name);
CREATE INDEX IF NOT EXISTS idx_checklists_plate ON checklists(transport_plate);
CREATE INDEX IF NOT EXISTS idx_checklists_date ON checklists(check_date);
CREATE INDEX IF NOT EXISTS idx_items_checklist ON checklist_items(checklist_id);
