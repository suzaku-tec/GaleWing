import { IElementEvent } from './elementEvent';
import { Modal } from 'bootstrap';
import axios from 'axios';

export default class AddSiteEvent implements IElementEvent {
  execute() {
    var modalBody = document.getElementById('modal-body');
    var modalFooter = document.getElementById('modal-footer');

    var modalAddSiteBody = document.getElementById('modalAddSiteBody');
    var modalAddSiteFooter = document.getElementById('modalAddSiteFooter');

    modalBody.appendChild(modalAddSiteBody.cloneNode(true));
    modalFooter.appendChild(modalAddSiteFooter.cloneNode(true));

    modalFooter.getElementsByClassName('modal-submit')[0].addEventListener('click', () => {
      var link = (modalBody.getElementsByClassName('addSiteUrl')[0] as HTMLInputElement).value;
      var uri = new URL(window.location.href);
      axios
        .post(uri.origin + '/addFeed', {
          link: link,
        })
        .then((response) => {
          console.log(response.data);

          modal.hide();
        })
        .catch((error) => {
          modal.hide();
        });
    });

    var modal = new Modal(document.getElementById('exampleModal'));
    modal.show();

    document.getElementById('exampleModal').addEventListener(
      'hidden.bs.modal',
      (event) => {
        var modalBody = document.getElementById('modal-body');
        while (modalBody.firstChild) {
          modalBody.removeChild(modalBody.firstChild);
        }

        var modalFooter = document.getElementById('modal-footer');
        while (modalFooter.firstChild) {
          modalFooter.removeChild(modalFooter.firstChild);
        }

        modal.dispose();
      },
      { once: true },
    );
  }
}
