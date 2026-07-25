import { Modal } from 'bootstrap';

let modal: Modal;
/**
 * Shows a modal dialog with the specified type and title, and adds submit functions to the footer.
 * @param type - The type of the modal dialog.
 * @param title - The title of the modal dialog.
 * @param submitFuncs - An array of functions to be called when the submit buttons in the footer are clicked.
 * @returns void
 */
export function showModal(type: string, title: string, submitFuncs: ((modal: Modal, modalBody: HTMLElement) => void)[]): void {

  const { typeModalBody, typeModalFooter } = initModal(type, title);

  modal = new Modal(document.getElementById('modal')!);

  submitFuncs.forEach((func, index) => {
    typeModalFooter.getElementsByClassName('modal-submit')[index].addEventListener('click', () => {
      func(modal, typeModalBody);
    });
  });

  modal.show();
  document
    .getElementById('modal')!
    .addEventListener('hidden.bs.modal', disposeModal(modal), { once: true });

}

export function showExecModal(type: string, title: string, exec: () => void): void {
  initModal(type, title);
  modal = new Modal(document.getElementById('modal')!);
  modal.show();
  document
    .getElementById('modal')!
    .addEventListener('hidden.bs.modal', disposeModal(modal), { once: true });
}

export function showExecFuncModal(type: string, title: string, exec: (modalBody: HTMLElement) => void, submitFuncs: ((modal: Modal, modalBody: HTMLElement) => void)[]): void {
  const { typeModalBody, typeModalFooter } = initModal(type, title);
  exec(typeModalBody);
  modal = new Modal(document.getElementById('modal')!);
  modal.show();
  submitFuncs.forEach((func, index) => {
    if (typeModalFooter.getElementsByClassName('modal-submit').item(index)) {
      typeModalFooter.getElementsByClassName('modal-submit').item(index)?.addEventListener('click', () => {
        func(modal, typeModalBody);
      });
    }
  });
  document
    .getElementById('modal')!
    .addEventListener('hidden.bs.modal', disposeModal(modal), { once: true });
}

export function closeModal(): void {
  if (modal) {
    disposeModal(modal);
  }
}

function initModal(type: string, title: string): { typeModalBody: HTMLElement; typeModalFooter: HTMLElement } {
  const modalBody = document.getElementById('modal-body')!;
  while (modalBody.firstChild) {
    modalBody.removeChild(modalBody.firstChild);
  }

  const modalFooter = document.getElementById('modal-footer')!;
  while (modalFooter.firstChild) {
    modalFooter.removeChild(modalFooter.firstChild);
  }

  const typeModalBody = document.getElementById(type + 'ModalBody')!;
  const typeModalFooter = document.getElementById(type + 'ModalFooter')!;

  const modalTitle = document.getElementById('modalTitle')!;
  modalTitle.textContent = title;

  const cloneTypeModalBody = typeModalBody.cloneNode(true) as HTMLElement;
  const cloneTypeModalFooter = typeModalFooter.cloneNode(true) as HTMLElement;

  modalBody.appendChild(cloneTypeModalBody);
  modalFooter.appendChild(cloneTypeModalFooter);

  return { typeModalBody: cloneTypeModalBody, typeModalFooter: cloneTypeModalFooter };
}

function disposeModal(modal: Modal): EventListenerOrEventListenerObject {
  return (event) => {
    const modalBody = document.getElementById('modal-body')!;
    while (modalBody.firstChild) {
      modalBody.removeChild(modalBody.firstChild);
    }

    const modalFooter = document.getElementById('modal-footer')!;
    while (modalFooter.firstChild) {
      modalFooter.removeChild(modalFooter.firstChild);
    }

    modal.dispose();
  };
}
