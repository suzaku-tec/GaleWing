import * as testTarget from '../galeWingApi';

jest.mock('axios');
import axios from 'axios';

const getReturnData = 'get return data';
const postReturnData = 'post return data';

(axios.get as any).mockResolvedValue({ data: getReturnData });
(axios.post as any).mockResolvedValue({ data: postReturnData });

describe('get test', () => {
  test('test getFeedList', async () => {
    const res = await testTarget.default.getInstance().getFeedList('https://test');
    expect(res.data).toEqual(getReturnData);
  });

  test('test getFeedList', async () => {
    const res = await testTarget.default.getInstance().getSiteList('https://test');
    expect(res.data).toEqual(getReturnData);
  });

  test('test getFeedList', async () => {
    const res = await testTarget.default.getInstance().getStackList('https://test');
    expect(res.data).toEqual(getReturnData);
  });
});

describe('post test', () => {
  test('test getFeedList', async () => {
    const res = await testTarget.default.getInstance().deleteSite('https://test', 'test');
    expect(res.data).toEqual(postReturnData);
  });

  test('test getFeedList', async () => {
    const res = await testTarget.default
      .getInstance()
      .stackFeed('https://test', 'testUuid', 'testLink');
    expect(res.data).toEqual(postReturnData);
  });
});
