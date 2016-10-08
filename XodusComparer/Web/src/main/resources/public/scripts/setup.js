app.controller('SetupController', function () {
});

app.controller('SetupNewController', function ($scope, $http, $location, status, errorHandler) {
    $scope.predefinedKeys = [
        {name: 'Hub', key: 'jetPassServerDb'},
        {name: 'YouTrack', key: 'teamsysstore'}
    ];

    $scope.applyKey = function (key, item) {
        $scope.data[key] = item.key;
    };

    $scope.data = {};

    $scope.submit = function () {
        if ($scope.form.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/compare', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        status.init({
                            success: function () {
                                $location.path('/tables');
                            }
                        });
                    })
                    .error(function (error) {
                        $scope.loading = false;
                        if (error.validationErrors) {
                            $scope.validationErrors = error.validationErrors;
                        } else {
                            errorHandler(error);
                        }
                    });
        }
    };
});

app.controller('SetupLoadController', function ($scope, $http, $location, status, errorHandler) {
    $scope.data = {};

    $scope.submit = function () {
        if ($scope.form.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/load', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        status.init({
                            success: function () {
                                $location.path('/tables');
                            }
                        });
                    })
                    .error(function (error) {
                        $scope.loading = false;
                        if (error.validationErrors) {
                            $scope.validationErrors = error.validationErrors;
                        } else {
                            errorHandler(error);
                        }
                    });
        }
    };
});