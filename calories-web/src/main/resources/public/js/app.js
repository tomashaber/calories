'use strict';

// declare top-level module which depends on filters,and services
var myApp = angular.module('myApp',
    ['ngRoute',
        'myApp.filters',
        'myApp.directives', // custom directives
        'ngGrid', // angular grid
        'ui', // angular ui
        'ngSanitize', // for html-bind in ckeditor
        'ui.bootstrap', // jquery ui bootstrap
        '$strap.directives', // angular strap
        'ngResource'
    ]);


var filters = angular.module('myApp.filters', []);
var directives = angular.module('myApp.directives', []);
var services = angular.module('myApp.services', ['ngResource']);

// bootstrap angular
myApp.config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {

    $routeProvider.when('/', {
        templateUrl: 'partials/home.html'
    });

    $routeProvider.when('/api', {
        templateUrl: 'partials/api.html',
        requireLogin: true
    });

    $routeProvider.when('/profile', {
        templateUrl: 'partials/profile.html',
        requireLogin: true
    });

    $routeProvider.when('/change-password', {
        templateUrl: 'partials/change-password.html',
        requireLogin: true
    });

    $routeProvider.when('/meals', {
        templateUrl: 'partials/meals.html',
        requireLogin: true
    });

    // by default, redirect to site root
    $routeProvider.otherwise({
        redirectTo: '/'
    });


}]);

// this is run after angular is instantiated and bootstrapped
myApp.run(function ($rootScope, $location, $http, $timeout, AuthService) {

    // *****
    // Initialize authentication
    // *****
    $rootScope.authService = AuthService;

    $rootScope.$on("$locationChangeStart", function (event, next) {
        var home = new RegExp('/#/$').test(next) || new RegExp('/#/api$').test(next);

        //tried to log in and failed - only / is available
        if (!$rootScope.authService.initialState()  && !$rootScope.authService.authorized() && !home) {
            $location.path('/');
            return;
        }
        //didn't try to log in yet
        if ($rootScope.authService.initialState()) {
            //try to log in
            $rootScope.authService.tryLogin(function (user) {
                if (user && user.id) {
                    //stay where you are
                } else {
                    //forward to /
                    $location.path('/');
                }
            });
        }
    });


    $rootScope.$watch('authService.authorized()', function () {

        // if never logged in, do nothing (otherwise bookmarks fail)
        if ($rootScope.authService.initialState()) {
            // we are public browsing
            return;
        }

        // instantiate and initialize an auth notification manager
        $rootScope.notifier = new NotificationManager($rootScope);

        // when user logs in, redirect to home
        if ($rootScope.authService.authorized()) {
            var currentUser = $rootScope.authService.currentUser();
            $rootScope.notifier.notify('information', 'Welcome ' + currentUser.username + "!");
        }

        // when user logs out, redirect to home
        if (!$rootScope.authService.authorized()) {
            $location.path("/");
            $rootScope.notifier.notify('information', 'Thanks for visiting.  You have been signed out.');
        }
    }, true);
});