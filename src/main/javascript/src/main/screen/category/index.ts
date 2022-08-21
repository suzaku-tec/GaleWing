import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import GaleWingApi from '../../api/galeWingApi';
import { faPlus, fas, faBars } from '@fortawesome/free-solid-svg-icons/index';
import ElementEvent from '../../events/elementEvent';
import AddCategoryEvent from '../../events/modal/addCategoryEvent';
library.add(faPlus, fas, faBars);
dom.watch();

function deleteCategory(uuid: string) {
  GaleWingApi.getInstance()
    .deleteCategory(uuid)
    .then(() => {
      location.reload();
    });
}

window.onload = () => {
  new ElementEvent(new AddCategoryEvent()).setup('click', document.getElementById('addCategory'));
};

export default { deleteCategory };
