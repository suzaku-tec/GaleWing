import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import GaleWingApi from '../../api/galeWingApi';
import { faPlus, fas, faBars } from '@fortawesome/free-solid-svg-icons/index';

library.add(faPlus, fas, faBars);
dom.watch();

window.onload = () => {
  document.getElementById('categoryAddBtn')?.addEventListener('click', () => {
    let siteUuid = (<HTMLInputElement>document.getElementById('siteUuid'))!.value;
    let categoryUuid = (<HTMLSelectElement>document.getElementById('categorySelect')).value;

    GaleWingApi.getInstance()
      .addSiteCategory(siteUuid, categoryUuid)
      .then((res) => {
        let categoryListEl = document.getElementById('categoryList')!;

        // 子要素の全削除
        while (categoryListEl.firstChild) {
          categoryListEl.removeChild(categoryListEl.firstChild);
        }

        res.data.forEach((element: { uuid: string; name: string }) => {
          let tagEl = createTagElement(element.name, element.uuid);
          categoryListEl?.appendChild(tagEl.label);
          categoryListEl?.appendChild(tagEl.closeBtn);
        });
      });
  });

  Array.from(document.getElementsByClassName('btn-close')).forEach((closeBtnEl) => {
    closeBtnEl.addEventListener('click', () => {
      let siteUuid = (<HTMLInputElement>document.getElementById('siteUuid'))!.value;
      let categoryUuid = (<HTMLElement>closeBtnEl).dataset.uuid!;

      GaleWingApi.getInstance()
        .deleteSiteCategory(siteUuid, categoryUuid)
        .then(() => {
          // ボタン削除
          closeBtnEl.remove();

          // タグの削除
          Array.from(document.getElementsByClassName('label'))
            .filter((el) => categoryUuid === (<HTMLElement>el)!.dataset.uuid)
            .forEach((el) => {
              el.remove();
            });
        });
    });
  });
};

function createTagElement(categoryName: string, categoryUuid: string) {
  let labelDivEl = document.createElement('div');
  labelDivEl.className = 'label';
  labelDivEl.innerText = categoryName;
  labelDivEl.dataset.uuid = categoryUuid;

  let closeBtn = <HTMLButtonElement>document.createElement('button');
  closeBtn.type = 'button';
  closeBtn.className = 'btn-close';
  closeBtn.ariaLabel = 'Close';
  labelDivEl.dataset.uuid = categoryUuid;

  return { label: labelDivEl, closeBtn: closeBtn };
}
