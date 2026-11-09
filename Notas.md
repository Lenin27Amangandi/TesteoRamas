# ==========================================================
# RED BAYESIANA: RAIN - MAINTENANCE - TRAIN - APPOINTMENT
# ==========================================================

!pip install -q pgmpy

from pgmpy.models import DiscreteBayesianNetwork
from pgmpy.factors.discrete import TabularCPD
from pgmpy.inference import VariableElimination

# 1. ESTRUCTURA DE LA RED
# Rain -> Maintenance
# Rain -> Train
# Maintenance -> Train
# Train -> Appointment

model = DiscreteBayesianNetwork([
    ('Rain', 'Maintenance'),
    ('Rain', 'Train'),
    ('Maintenance', 'Train'),
    ('Train', 'Appointment')
])

# 2. TABLAS DE PROBABILIDAD CONDICIONAL

# P(Rain)
cpd_rain = TabularCPD(
    variable='Rain',
    variable_card=3,
    values=[[0.7], [0.2], [0.1]],
    state_names={
        'Rain': ['none', 'light', 'heavy']
    }
)

# P(Maintenance | Rain)
cpd_maintenance = TabularCPD(
    variable='Maintenance',
    variable_card=2,
    values=[
        [0.4, 0.2, 0.1],  # yes
        [0.6, 0.8, 0.9]   # no
    ],
    evidence=['Rain'],
    evidence_card=[3],
    state_names={
        'Maintenance': ['yes', 'no'],
        'Rain': ['none', 'light', 'heavy']
    }
)

# P(Train | Rain, Maintenance)
# Columnas:
# none/yes, none/no, light/yes,
# light/no, heavy/yes, heavy/no

cpd_train = TabularCPD(
    variable='Train',
    variable_card=2,
    values=[
        [0.8, 0.9, 0.6, 0.7, 0.4, 0.5],
        [0.2, 0.1, 0.4, 0.3, 0.6, 0.5]
    ],
    evidence=['Rain', 'Maintenance'],
    evidence_card=[3, 2],
    state_names={
        'Train': ['on time', 'delayed'],
        'Rain': ['none', 'light', 'heavy'],
        'Maintenance': ['yes', 'no']
    }
)

# P(Appointment | Train)
cpd_appointment = TabularCPD(
    variable='Appointment',
    variable_card=2,
    values=[
        [0.9, 0.6],  # attend
        [0.1, 0.4]   # miss
    ],
    evidence=['Train'],
    evidence_card=[2],
    state_names={
        'Appointment': ['attend', 'miss'],
        'Train': ['on time', 'delayed']
    }
)

# 3. CONSTRUIR Y VALIDAR LA RED

model.add_cpds(
    cpd_rain,
    cpd_maintenance,
    cpd_train,
    cpd_appointment
)

assert model.check_model(), "Error en las CPT"

print("RED BAYESIANA VÁLIDA")

# 4. INFERENCIA AUTOMÁTICA CON PGMPY

inference = VariableElimination(model)

resultado = inference.query(
    variables=['Appointment'],
    evidence={
        'Rain': 'light',
        'Maintenance': 'no'
    }
)

print("\nRESULTADO CON PGMPY")
print(resultado)

# 5. MÉTODO MANUAL:
# PROBABILIDADES CONJUNTAS + NORMALIZACIÓN

print("\nMÉTODO DE PROBABILIDADES CONJUNTAS")

p_light = 0.2
p_no_light = 0.8

# Marginalización sobre Train para MISS
suma_miss = (
    0.7 * 0.1 +   # on time
    0.3 * 0.4     # delayed
)

# Marginalización sobre Train para ATTEND
suma_attend = (
    0.7 * 0.9 +   # on time
    0.3 * 0.6     # delayed
)

# Probabilidades conjuntas
p_miss_joint = p_light * p_no_light * suma_miss
p_attend_joint = p_light * p_no_light * suma_attend

# Constante de normalización
alpha = 1 / (p_miss_joint + p_attend_joint)

# Probabilidades condicionales
p_miss = alpha * p_miss_joint
p_attend = alpha * p_attend_joint

print("P(miss, light, no) =", round(p_miss_joint, 4))
print("P(attend, light, no) =", round(p_attend_joint, 4))
print("Alpha =", round(alpha, 4))
print("P(miss | light, no) =", round(p_miss, 4))
print("P(attend | light, no) =", round(p_attend, 4))

# 6. COMPARACIÓN DE RESULTADOS

p_pgmpy = resultado.get_value(Appointment='miss')

assert abs(p_pgmpy - p_miss) < 1e-10

print("\nVERIFICACIÓN FINAL")
print("Resultado manual:", round(p_miss, 4))
print("Resultado pgmpy:", round(p_pgmpy, 4))
print("Ambos métodos coinciden.")


## Number 2

# ==========================================================
# RED BAYESIANA: ROBO - TERREMOTO - ALARMA - JOHN - MARY
# ==========================================================


from pgmpy.models import DiscreteBayesianNetwork
from pgmpy.factors.discrete import TabularCPD
from pgmpy.inference import VariableElimination

# 1. ESTRUCTURA DE LA RED

model = DiscreteBayesianNetwork([
    ('Robo', 'Alarma'),
    ('Terremoto', 'Alarma'),
    ('Alarma', 'JohnLlama'),
    ('Alarma', 'MaryLlama')
])

# 2. TABLAS DE PROBABILIDAD CONDICIONAL

# P(Robo)

cpd_robo = TabularCPD(
    variable='Robo',
    variable_card=2,
    values=[
        [0.001],
        [0.999]
    ],
    state_names={
        'Robo': ['true', 'false']
    }
)

# P(Terremoto)

cpd_terremoto = TabularCPD(
    variable='Terremoto',
    variable_card=2,
    values=[
        [0.002],
        [0.998]
    ],
    state_names={
        'Terremoto': ['true', 'false']
    }
)

# P(Alarma | Robo, Terremoto)
#
# Columnas:
# true/true, true/false,
# false/true, false/false

cpd_alarma = TabularCPD(
    variable='Alarma',
    variable_card=2,
    values=[
        [0.95, 0.94, 0.29, 0.001],
        [0.05, 0.06, 0.71, 0.999]
    ],
    evidence=['Robo', 'Terremoto'],
    evidence_card=[2, 2],
    state_names={
        'Alarma': ['true', 'false'],
        'Robo': ['true', 'false'],
        'Terremoto': ['true', 'false']
    }
)

# P(JohnLlama | Alarma)

cpd_john = TabularCPD(
    variable='JohnLlama',
    variable_card=2,
    values=[
        [0.90, 0.05],
        [0.10, 0.95]
    ],
    evidence=['Alarma'],
    evidence_card=[2],
    state_names={
        'JohnLlama': ['true', 'false'],
        'Alarma': ['true', 'false']
    }
)

# P(MaryLlama | Alarma)

cpd_mary = TabularCPD(
    variable='MaryLlama',
    variable_card=2,
    values=[
        [0.70, 0.01],
        [0.30, 0.99]
    ],
    evidence=['Alarma'],
    evidence_card=[2],
    state_names={
        'MaryLlama': ['true', 'false'],
        'Alarma': ['true', 'false']
    }
)

# 3. CONSTRUIR Y VALIDAR LA RED

model.add_cpds(
    cpd_robo,
    cpd_terremoto,
    cpd_alarma,
    cpd_john,
    cpd_mary
)

assert model.check_model(), "Error en las CPT"

print("RED BAYESIANA VÁLIDA")

# 4. INFERENCIA AUTOMÁTICA

inference = VariableElimination(model)

# Ejemplo: calcular P(Robo | John llama, Mary llama)

resultado = inference.query(
    variables=['Robo'],
    evidence={
        'JohnLlama': 'true',
        'MaryLlama': 'true'
    }
)

print("\nRESULTADO CON PGMPY")
print(resultado)
