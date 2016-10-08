app.factory('status', function ($http, $location) {
    return {
        init: function (options) {

            options = (options || {});

            var successCallback = (options.success || (function () { }));

            $http
                    .post('/api/v1/utils/status', {})
                    .success(function (data) {
                        app.status = data;
                        if (!app.status.inited) {
                            if ($location.path().indexOf('/setup') === -1) {
                                $location.path('/setup');
                            }
                        } else {
                            successCallback();
                        }
                    });
        }
    };
});