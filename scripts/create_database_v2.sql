-- Version 2: Added descripcion field to notas table

-- Add descripcion column to notas table if it doesn't exist
ALTER TABLE notas 
ADD COLUMN IF NOT EXISTS descripcion TEXT;

-- Update existing records to have empty descripcion
UPDATE notas SET descripcion = '' WHERE descripcion IS NULL;
