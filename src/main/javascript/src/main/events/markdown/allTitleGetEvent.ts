import GaleWingGrid from "../../screen/feed/galeWingGrid";

export default class AllTitleGetEvent {
  execute(): void {

    const mdTitleList = GaleWingGrid.getInstance().data.map((item: any) => {
      return "- " + item.title;
    }).join('\r\n');

    navigator.clipboard.writeText(mdTitleList);
  }
}
