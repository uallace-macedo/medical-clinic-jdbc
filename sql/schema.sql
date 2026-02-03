CREATE TABLE IF NOT EXISTS specialties (
  id INT AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS doctors (
  id INT AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  crm VARCHAR(20) UNIQUE NOT NULL,
  specialty_id INT NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT fk_doctor_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id)
);

CREATE TABLE IF NOT EXISTS patients (
  id INT AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) UNIQUE NOT NULL,
  telephone VARCHAR(15) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS appointments (
  id INT AUTO_INCREMENT,
  doctor_id INT NOT NULL,
  patient_id INT NOT NULL,
  appointment_date DATETIME NOT NULL,
  status ENUM ('SCHEDULED', 'CANCELED', 'FINISHED') DEFAULT 'SCHEDULED',

  PRIMARY KEY (id),
  CONSTRAINT uk_doctor_schedule UNIQUE (doctor_id, appointment_date),
  CONSTRAINT uk_patient_schedule UNIQUE (patient_id, appointment_date),
  CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id),
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients (id)
);