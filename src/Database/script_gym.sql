-- Script para PostgreSQL - Sistema "Personal Training"
-- Diseño adaptado directamente del diagrama físico del proyecto guía (páginas 16-17)

-- 1. Tabla Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id SERIAL PRIMARY KEY,
    altura REAL NOT NULL,
    correo VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    peso REAL NOT NULL,
    sexo VARCHAR(50) NOT NULL,
    telefono VARCHAR(50) NOT NULL
);

-- 2. Tabla Dieta
CREATE TABLE IF NOT EXISTS dieta (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL
);

-- 3. Tabla Ejercicio
CREATE TABLE IF NOT EXISTS ejercicio (
    id SERIAL PRIMARY KEY,
    imagen_url TEXT,
    duracion VARCHAR(100) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    repeticion VARCHAR(100) NOT NULL
);

-- 4. Tabla Rutina (Depende de Cliente y Dieta)
CREATE TABLE IF NOT EXISTS rutina (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    cliente_id INTEGER NOT NULL,
    dieta_id INTEGER NOT NULL,
    CONSTRAINT fk_rutina_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id) ON DELETE CASCADE,
    CONSTRAINT fk_rutina_dieta FOREIGN KEY (dieta_id) REFERENCES dieta (id) ON DELETE CASCADE
);

-- 5. Tabla Rutina_Ejercicio (Depende de Rutina y Ejercicio)
CREATE TABLE IF NOT EXISTS rutina_ejercicio (
    id SERIAL PRIMARY KEY,
    dia_rutina VARCHAR(100) NOT NULL,
    rutina_id INTEGER NOT NULL,
    ejercicio_id INTEGER NOT NULL,
    CONSTRAINT fk_re_rutina FOREIGN KEY (rutina_id) REFERENCES rutina (id) ON DELETE CASCADE,
    CONSTRAINT fk_re_ejercicio FOREIGN KEY (ejercicio_id) REFERENCES ejercicio (id) ON DELETE CASCADE
);
