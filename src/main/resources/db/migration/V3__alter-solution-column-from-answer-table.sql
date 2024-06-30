ALTER TABLE answers
ALTER
COLUMN solution TYPE boolean
USING solution::boolean;