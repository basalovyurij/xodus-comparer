app.controller('SetupController', function () {
});

app.controller('SetupNewController', function ($scope, $http, $location) {
    $scope.predefinedKeys = [
        {name: 'Hub', key: 'jetPassServerDb'},
        {name: 'YouTrack', key: 'teamsysstore'}
    ];

    $scope.applyKey = function (item) {
        $scope.data.key = item.key;
    };

    $scope.data = {};

    $scope.submit = function () {
        if ($scope.setupForm.$valid) {
            $http
                    .post('/api/v1/utils/compare', $scope.data)
                    .success(function (data) {
                        $location.path('/tables');
                    });
        }
    }
});

app.controller('SetupOldController', function ($scope, $http) {

});