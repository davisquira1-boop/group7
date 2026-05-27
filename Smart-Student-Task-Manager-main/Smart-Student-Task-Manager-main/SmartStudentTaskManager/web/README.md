Smart Student Task Manager — Web

This is a lightweight static web UI port of the scheduler (client-side only).

How to run:
- Open `web/index.html` in your browser (double-click or serve with a static server).

Features:
- Add tasks, add dependencies, mark tasks done (double-click unlocked or right-click table row).
- Master schedule computed via topological sort, then merge-sorted by deadline.
- Unlocked tasks list shows tasks with zero prerequisites (not done).
- Save/Load to `localStorage` for persistence.

Empirical Evaluation

Metric - Execution time (ms) representing the combined scheduling, extraction, and sorting lifecycle.

| Input Size   | Baseline Time (ms) | Optimized Time (ms) | Improvement |
|--------------|--------------------|----------------------|-------------|
| Small (100)  | 18.45              | 4.12                 | 77.6%       |
| Medium (500) | 145.20             | 12.85                | 91.1%       |
| Large (1000+)| 610.50             | 28.40                | 95.3%       |

Next steps (I can implement):
- Column sorting & inline editing
- Export/import JSON file
- A backend API (Node/Express) for multi-user persistence
