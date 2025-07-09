CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
-- For PostgreSQL
ALTER TABLE users
ADD COLUMN account_non_expired BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN account_non_locked BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN credentials_non_expired BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT true;
-- Update existing users to be enabled and verified
UPDATE users
SET account_non_expired = true,
    account_non_locked = true,
    credentials_non_expired = true,
    enabled = true;
ALTER TABLE users DROP COLUMN IF EXISTS account_non_expired,
    DROP COLUMN IF EXISTS account_non_locked,
    DROP COLUMN IF EXISTS credentials_non_expired,
    DROP COLUMN IF EXISTS enabled;