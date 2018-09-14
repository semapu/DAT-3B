
// Controller for the account view
webMsgs.controller('InboxController', ['$scope', 'remoteMsgs', function($scope, remoteMsgs) {
    //-----------------------------------------
    // Data
    $scope.receivedMessages = null;
    $scope.selectedMessages = []; // array of message ids
    $scope.messageDetail = null; // message object

    //-----------------------------------------
    // Operations

    // toggle selection for a given message
    $scope.toggleSelectedMessage = function(msg) {
        $scope.error = null;
        var idx = $scope.selectedMessages.indexOf(msg.id);

        if (idx > -1) { // is currently selected
            $scope.selectedMessages.splice(idx, 1);
        } else {        // is newly selected
            $scope.selectedMessages.push(msg.id);
        }
    };

    $scope.deleteMessages = function(mids) {
        $scope.error = 'TODO: delete messages: ' + mids;
    };

    $scope.showMessage = function(mid) {
        $scope.error = 'TODO: show message details: ' + mid;
    };

    function reloadAccount() {
        remoteMsgs.getAccount($scope.user.name,
            function(account) {
                $scope.receivedMessages = account.received;
                $scope.selectedMessages = [];
            }
        );
    }

    //-----------------------------------------
    // Initial load
    reloadAccount();

}]);

