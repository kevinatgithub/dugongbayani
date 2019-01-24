/**
 * First we will load all of this project's JavaScript dependencies which
 * includes Vue and other libraries. It is a great starting point when
 * building robust, powerful web applications using Vue and Laravel.
 */

import _ from 'lodash';
import ioClient from 'socket.io-client';

require('./bootstrap');

window.Vue = require('vue');

Vue.component('editor', require('./components/Editor.vue'));


const app = new Vue({
    el: '#app',
    template: `<editor />`
});