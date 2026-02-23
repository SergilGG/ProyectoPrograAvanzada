import matplotlib.pyplot as plt
import numpy as np

# ---------------------------------------------------------
tasks = [10000, 50000, 100000]
# ---------------------------------------------------------

#tiempo hilos de plataforma
runs_time_plat = [
    [13738, 66812, 165589],
    [13734, 66677, 132869],
    [13780, 66676, 133077]
]

#tiempohilos virtuales
runs_time_virt = [
    [7472, 22757, 43116],
    [7281, 18859, 44456],
    [7341, 18485, 36251]
]

#consumo de memoria plataforma
runs_mem_plat = [
    [10.00, 8.00, 14.00],
    [12.00, 8.00, 17.67],
    [10.00, 12.00, 14.00]
]

#consumo de memoria virtuales
runs_mem_virt = [
    [48.00, 60.98, 76.00],
    [50.00, 58.00, 76.00],
    [46.00, 56.00, 80.00]
]

# ------------------------------------------------------
avg_time_plat = np.mean(runs_time_plat, axis=0)
avg_time_virt = np.mean(runs_time_virt, axis=0)
avg_mem_plat = np.mean(runs_mem_plat, axis=0)
avg_mem_virt = np.mean(runs_mem_virt, axis=0)

#grafica de tiempo de ejecucion promedio
plt.figure(figsize=(8, 5))
plt.plot(tasks, avg_time_plat, marker='o', label='Hilos de Plataforma', linewidth=2, color='#e74c3c')
plt.plot(tasks, avg_time_virt, marker='s', label='Hilos Virtuales (Loom)', linewidth=2, color='#2ecc71')
plt.title('Tiempo de Ejecución Promedio vs Número de Tareas', fontsize=14)
plt.xlabel('Número de Tareas', fontsize=12)
plt.ylabel('Tiempo Promedio (ms)', fontsize=12)
plt.xticks(tasks, ['10k', '50k', '100k'])
plt.grid(True, linestyle='--', alpha=0.7)
plt.legend(fontsize=11)
plt.tight_layout()
plt.savefig('tiempo_ejecucion_promedio.png', dpi=300)

#grafica de consumo de memoria promedio
x = np.arange(len(tasks))
width = 0.35
plt.figure(figsize=(8, 5))
plt.bar(x - width/2, avg_mem_plat, width, label='Hilos de Plataforma', color='#e74c3c')
plt.bar(x + width/2, avg_mem_virt, width, label='Hilos Virtuales', color='#2ecc71')
plt.title('Consumo de Memoria Promedio vs Número de Tareas', fontsize=14)
plt.xlabel('Número de Tareas', fontsize=12)
plt.ylabel('Memoria Estimada Promedio (MB)', fontsize=12)
plt.xticks(x, ['10k', '50k', '100k'])
plt.legend(fontsize=11)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()
plt.savefig('consumo_memoria_promedio.png', dpi=300)

#grafica de speedup
speedup = [p/v for p, v in zip(avg_time_plat, avg_time_virt)]
plt.figure(figsize=(8, 5))
plt.plot(tasks, speedup, marker='^', color='#3498db', linewidth=2, markersize=8)
plt.title('Speedup Promedio (Plataforma / Virtuales)', fontsize=14)
plt.xlabel('Número de Tareas', fontsize=12)
plt.ylabel('Speedup (X veces más rápido)', fontsize=12)
plt.xticks(tasks, ['10k', '50k', '100k'])
plt.grid(True, linestyle='--', alpha=0.7)
for i, txt in enumerate(speedup):
    plt.annotate(f"{txt:.2f}x", (tasks[i], speedup[i]), textcoords="offset points", xytext=(0,10), ha='center', fontsize=11, fontweight='bold')
plt.tight_layout()
plt.savefig('speedup_promedio.png', dpi=300)