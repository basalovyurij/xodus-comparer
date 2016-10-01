app.directive('footer', function ($uibModal) {
    return {
        restrict: 'E',
        templateUrl: '../../views/footer.html',
        link: function (scope) {
            scope.status = app.status;
            
            scope.showModal = function (size) {
                $uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: '../../views/modal.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: '$ctrl',
                    size: size,
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

app.controller('ModalInstanceCtrl', function ($uibModalInstance) {
    var $ctrl = this;
    $ctrl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});