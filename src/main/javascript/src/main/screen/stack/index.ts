import 'bootstrap/dist/css/bootstrap.min.css';
import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';
import { Grid } from 'gridjs';

import GaleWingApi from '../../api/galeWingApi';

window.onload = () => {
  var api = GaleWingApi.getInstance();

  api.getStackList(window.location.href).then((res) => {
    console.log(res.data);
    var grid = new Grid({
      columns: [
        { name: 'title' },
        { name: 'uuid', hidden: true },
        { name: 'link', hidden: true },
        { name: 'uri', hidden: true },
        { name: 'author', hidden: true },
        { name: 'comments', hidden: true },
        { name: 'publishedDate' },
        { name: 'stackDate' },
      ],
      pagination: true,
      sort: true,
      search: true,
      data: res.data,
    }).render(document.getElementById('wrapper')!);
  });
};

export default { hideModifier };
