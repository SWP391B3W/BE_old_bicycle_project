ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS buyer_confirmation_deadline TIMESTAMP;

UPDATE orders
SET buyer_confirmation_deadline = COALESCE(updated_at, created_at) + INTERVAL '5 days'
WHERE status = 'awaiting_buyer_confirmation'
  AND funding_status = 'held'
  AND buyer_confirmation_deadline IS NULL;
