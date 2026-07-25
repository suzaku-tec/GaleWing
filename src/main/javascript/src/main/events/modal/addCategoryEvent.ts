import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';
import GaleWingApi from '../../api/galeWingApi';

export default class AddCategoryEvent implements IElementEvent {
  execute(): void {
    const modal = new Modal(document.getElementById('exampleModal')!);

    const executeBtn = document.getElementById('execute')!;
    executeBtn.addEventListener('click', () => {
      const nameEl = <HTMLInputElement>document.getElementById('name')!;
      const descriptionEl = <HTMLInputElement>document.getElementById('description')!;
      GaleWingApi.getInstance()
        .addCategory(nameEl.value, descriptionEl.value)
        .then(() => {
          nameEl.value = '';
          descriptionEl.value = '';
          modal.hide();
        })
        .catch(() => {
          modal.hide();
        });
    });

    document.getElementById('exampleModal')?.addEventListener(
      'hidden.bs.modal',
      () => {
        modal.dispose();
      },
      { once: true },
    );

    modal.show();
  }
}
