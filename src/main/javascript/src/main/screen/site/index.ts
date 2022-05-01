import 'bootstrap/dist/css/bootstrap.min.css';
import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';

import GaleWingApi from '../../api/galeWingApi';

function deleteSite(uuid: string) {
  var api = GaleWingApi.getInstance();
  var url = window.location.href;
  return api.deleteSite(url, uuid).then((res) => {
    location.reload();
  });
}

window.onload = () => {
  var feedListBtn = document.getElementById('feedList');
  feedListBtn.addEventListener('click', () => {
    var url = new URL(location.href);
    location.href = url.origin;
  });

  var feedUpdate = document.getElementById('feedUpdate');
  feedUpdate.addEventListener('click', () => {
    window.location.href = '/site/lastUpdateDate';
  });
};

export default { hideModifier, deleteSite };
