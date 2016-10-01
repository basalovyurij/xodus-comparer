var app = angular.module('todoapp', [
    'ngRoute',
    'ui.bootstrap'
]);

app.config(function ($routeProvider) {
    $routeProvider
            .when('/setup', {
                templateUrl: 'views/setup.html',
                controller: 'SetupController'
            })
            .when('/setup/new', {
                templateUrl: 'views/setup-new.html',
                controller: 'SetupNewController'
            })
            .when('/setup/load', {
                templateUrl: 'views/setup-load.html',
                controller: 'SetupLoadController'
            })
            .when('/save', {
                templateUrl: 'views/save.html',
                controller: 'SaveController'
            })
            .when('/tables', {
                templateUrl: 'views/tables.html',
                controller: 'TablesController'
            })
            .when('/type/:name', {
                templateUrl: 'views/type.html',
                controller: 'TypeController'
            })
            .otherwise({
                redirectTo: '/setup'
            });
});

app.run(function (status) {
    status.init();
});