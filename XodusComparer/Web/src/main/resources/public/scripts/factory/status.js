app.factory('status', function ($http, $location) {
    return {
        init: function (options) {
            
            options = (options || {});
            
            var successCallback = (options.success || (function() {}));
                    
            $http
                    .post('/api/v1/utils/status', {})
                    .success(function (data) {
                        app.status = data;
                        if (!app.status.inited) {
                            $location.path('/setup');
                        } else {
                            successCallback();
                        }
                    });
        }
    };
});