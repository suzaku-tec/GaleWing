import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';
import GaleWingApi from '../../api/galeWingApi';

export default class AddCategoryEvent implements IElementEvent {
  execute(): void {
    let modal = new Modal(document.getElementById('exampleModal')!);

    let executeBtn = document.getElementById('execute')!;
    executeBtn.addEventListener('click', () => {
      let nameEl = <HTMLInputElement>document.getElementById('name')!;
      let descriptionEl = <HTMLInputElement>document.getElementById('description')!;
      GaleWingApi.getInstance()
        .addCategory(nameEl.value, descriptionEl.value)
        .then(() => {
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
