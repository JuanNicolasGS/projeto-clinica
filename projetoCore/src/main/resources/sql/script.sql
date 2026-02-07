CREATE TABLE clinica (
    id_clinica SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);


CREATE TABLE profissional (
    id_profissional SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100),
    registro VARCHAR(50) UNIQUE NOT NULL,
    id_clinica INT NOT NULL,
    FOREIGN KEY (id_clinica) REFERENCES clinica(id_clinica)
);


CREATE TABLE profissional_telefone (
    id_profissional INT NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_profissional, telefone),
    FOREIGN KEY (id_profissional) REFERENCES profissional(id_profissional)
);


CREATE TABLE paciente (
    cpf CHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_nasc DATE NOT NULL
);


CREATE TABLE paciente_telefone (
    cpf CHAR(11) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    PRIMARY KEY (cpf, telefone),
    FOREIGN KEY (cpf) REFERENCES paciente(cpf)
);


CREATE TABLE consulta (
    id_consulta SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    valor NUMERIC(10,2),
    observacoes TEXT,
    cpf_paciente CHAR(11) NOT NULL,
    id_profissional INT NOT NULL,
    id_clinica INT NOT NULL,
    FOREIGN KEY (cpf_paciente) REFERENCES paciente(cpf),
    FOREIGN KEY (id_profissional) REFERENCES profissional(id_profissional),
    FOREIGN KEY (id_clinica) REFERENCES clinica(id_clinica)
);


CREATE TABLE exame (
    id_exame SERIAL PRIMARY KEY,
    tipo_exame VARCHAR(100) NOT NULL,
    prioridade VARCHAR(50),
    observacoes TEXT,
    cpf_paciente CHAR(11) NOT NULL,
    id_profissional INT NOT NULL,
    FOREIGN KEY (cpf_paciente) REFERENCES paciente(cpf),
    FOREIGN KEY (id_profissional) REFERENCES profissional(id_profissional)
);

CREATE TABLE receita (
    id_receita SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    validade DATE,
    observacoes TEXT,
    id_consulta INT NOT NULL,
    FOREIGN KEY (id_consulta) REFERENCES consulta(id_consulta)
);


CREATE TABLE medicamento (
    id_medicamento SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);


CREATE TABLE receita_medicamento (
    id_receita INT NOT NULL,
    id_medicamento INT NOT NULL,
    PRIMARY KEY (id_receita, id_medicamento),
    FOREIGN KEY (id_receita) REFERENCES receita(id_receita),
    FOREIGN KEY (id_medicamento) REFERENCES medicamento(id_medicamento)
);

SELECT * FROM paciente;


