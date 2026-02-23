-- Migration script for Chat system refactoring
-- Execute this in your PostgreSQL database

-- Step 1: Drop old table chats (will lose existing chat data)
DROP TABLE IF EXISTS chats CASCADE;

-- Step 2: Create conversations table
CREATE TABLE IF NOT EXISTS conversations (
    id SERIAL PRIMARY KEY,
    user1_id INTEGER NOT NULL,
    user2_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_message_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Create new chats table
CREATE TABLE chats (
    id SERIAL PRIMARY KEY,
    conversation_id INTEGER NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id INTEGER NOT NULL,
    message_text TEXT NOT NULL,
    file_attach VARCHAR(255),
    is_read BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Step 4: Create indexes for better performance
CREATE INDEX idx_conversations_user1 ON conversations(user1_id);
CREATE INDEX idx_conversations_user2 ON conversations(user2_id);
CREATE INDEX idx_conversations_last_message ON conversations(last_message_at DESC);
CREATE INDEX idx_chats_conversation ON chats(conversation_id);
CREATE INDEX idx_chats_sender ON chats(sender_id);
CREATE INDEX idx_chats_sent_at ON chats(sent_at);

-- Step 5: Add lastSeen column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
