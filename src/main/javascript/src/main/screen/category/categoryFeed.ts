import { uuid } from './../../../@types/readedRes.d';
import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import { fas, faBars } from '@fortawesome/free-solid-svg-icons/index';
import GaleWingApi from '../../api/galeWingApi';
library.add(fas, faBars);
dom.watch();

import { Grid, html } from 'gridjs';
import SettingApi from '../../api/settingApi';
var setting: SettingApi;

window.onload = async () => {
  setting = new SettingApi();
  await setting.init();
  setting.outputLog();

  let categoryList = Array.from(document.getElementsByName('category')).map(
    (item) => (<HTMLInputElement>item).value,
  );

  if (categoryList) {
    let api = GaleWingApi.getInstance();
    api.categoryFeed(categoryList).then((res) => {
      let grid = new Grid({
        columns: [
          {
            name: 'title',
            hidden: false,
          },
          { name: 'uuid', hidden: true },
          { name: 'link', hidden: true },
          { name: 'uri', hidden: true },
          { name: 'author', hidden: true },
          { name: 'comments', hidden: true },
          { name: 'publishedDate', hidden: true },
          { name: 'opened', hidden: true },
          { name: 'readed', hidden: true },
        ],
        pagination: {
          enabled: true,
          limit: Number(setting.get('feed_rows')),
        },
        sort: true,
        search: true,
        data: res.data,
      }).render(<HTMLInputElement>document.getElementById('feed'));
    });
  }

  Array.from(document.getElementsByClassName('btn-close')).forEach((closeBtnEl) => {
    let categoryUuid = (<HTMLElement>closeBtnEl).dataset.uuid!;
    closeBtnEl.addEventListener('click', () => {
      closeBtnEl.remove();
      Array.from(document.getElementsByClassName('label'))
        .filter((el) => categoryUuid === (<HTMLElement>el)!.dataset.uuid)
        .forEach((el) => {
          el.remove();
        });
    });
  });

  document.getElementById('categoryAddBtn')?.addEventListener('click', () => {
    let categorySelect = <HTMLSelectElement>document.getElementById('categorySelect');

    if (categorySelect) {
      let categoryListEl = document.getElementById('categoryList')!;

      let tagEl = createTagElement(categorySelect.innerText, categorySelect.value);
      categoryListEl?.appendChild(tagEl.label);
      categoryListEl?.appendChild(tagEl.closeBtn);
    }
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

function deleteCategoryTag(categoryUuid: string): void {
  // タグの削除
  Array.from(document.getElementsByClassName('label'))
    .filter((el) => categoryUuid === (<HTMLElement>el)!.dataset.uuid)
    .forEach((el) => {
      el.remove();
    });
}
