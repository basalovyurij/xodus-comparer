app.directive('footer', function ($uibModal) {
    return {
        restrict: 'E',
        templateUrl: '../../views/footer.html',
        link: function (scope) {
            scope.status = app.status;
            
            scope.showModal = function () {
                $uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: '../../views/info-modal.html',
                    controller: 'InfoModalInstanceCtrl',
                    controllerAs: '$ctrl',
                    resolve: {
                        status: function () {
                            return app.status;
                        }
                    }
                });
            };
        }
    };
});

app.controller('InfoModalInstanceCtrl', function ($uibModalInstance) {
    var $ctrl = this;
    $ctrl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});