!function( window ) {
    const ajax = {
        _request: null,
        init: function (config) {
            _request = new XMLHttpRequest();
            _request.open(config.type, config.url);
            _request.send();
            return this;
        },

        done: function (callback) {
            _request.onload = function (event) {
                callback(_request.response, _request.readyState );
            };
            return this;
        },

        fail: function (callback) {
            _request.onerror = callback;
            return this;
        }
    }

    window.ajax = function(config) {
        return ajax.init(config);
    }
}( window );