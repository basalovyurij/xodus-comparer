app.factory('status', function ($http, $location) {
    return {
        init: function () {
            $http
                    .post('/api/v1/utils/status', {})
                    .success(function (data) {
                        app.status = data;
                        if (!app.status.inited) {
                            $location.path('/setup');
                        }
                    });
        }
    };
});