import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { faBars } from "@fortawesome/free-solid-svg-icons/faBars";

library.add(faBars);
dom.watch();

import { Grid } from "gridjs";
import "gridjs/dist/theme/mermaid.css";

import hideModifier from '@popperjs/core/lib/modifiers/hide';

export default { Grid, hideModifier }

