import GaleWingApi from '../../api/galeWingApi';
import 'bootstrap/dist/css/bootstrap.min.css';
document.addEventListener('DOMContentLoaded', () => {
  const executeBtn = document.getElementById('executeBtn');
  executeBtn?.addEventListener('click', () => {
    const taskNameSelEl = <HTMLSelectElement>document.getElementById('taskName');
    const taskName = taskNameSelEl.value;
    console.log('taskName:', taskName);

    if (taskName) {
      const api = GaleWingApi.getInstance();
      api.executeTask(taskName);
    }
  });
});
