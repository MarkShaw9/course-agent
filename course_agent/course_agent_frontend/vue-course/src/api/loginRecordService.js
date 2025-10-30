import axios from 'axios';

const API_URL = '/api/admin/login-records';

export default {

  getAllLoginRecords() {
    return axios.get(API_URL);
  },

  getLoginRecordsByUsername(username) {
    return axios.get(`${API_URL}/${username}`);
  }
};
