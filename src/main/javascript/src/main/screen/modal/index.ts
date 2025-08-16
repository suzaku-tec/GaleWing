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

  let { modalBody, modalFooter } = initModal(type, title);

  modal = new Modal(document.getElementById('modal')!);

  submitFuncs.forEach((func, index) => {
    modalFooter.getElementsByClassName('modal-submit')[index].addEventListener('click', () => {
      func(modal, modalBody);
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
  let { modalBody, modalFooter } = initModal(type, title);
  exec(modalBody);
  modal = new Modal(document.getElementById('modal')!);
  modal.show();
  submitFuncs.forEach((func, index) => {
    modalFooter.getElementsByClassName('modal-submit')[index].addEventListener('click', () => {
      func(modal, modalBody);
    });
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

function initModal(type: string, title: string): { modalBody: HTMLElement; modalFooter: HTMLElement } {
  let modalBody = document.getElementById('modal-body')!;
  while (modalBody.firstChild) {
    modalBody.removeChild(modalBody.firstChild);
  }

  let modalFooter = document.getElementById('modal-footer')!;
  while (modalFooter.firstChild) {
    modalFooter.removeChild(modalFooter.firstChild);
  }

  let typeModalBody = document.getElementById(type + 'ModalBody')!;
  let typeModalFooter = document.getElementById(type + 'ModalFooter')!;

  let modalTitle = document.getElementById('modalTitle')!;
  modalTitle.textContent = title;

  modalBody.appendChild(typeModalBody.cloneNode(true));
  modalFooter.appendChild(typeModalFooter.cloneNode(true));

  return { modalBody, modalFooter };
}

function disposeModal(modal: Modal): EventListenerOrEventListenerObject {
  return (event) => {
    let modalBody = document.getElementById('modal-body')!;
    while (modalBody.firstChild) {
      modalBody.removeChild(modalBody.firstChild);
    }

    let modalFooter = document.getElementById('modal-footer')!;
    while (modalFooter.firstChild) {
      modalFooter.removeChild(modalFooter.firstChild);
    }

    modal.dispose();
  };
}
