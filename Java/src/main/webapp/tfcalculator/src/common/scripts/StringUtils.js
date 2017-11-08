// ReSharper disable NativeTypePrototypeExtending
(function () {
    String.prototype.startWith = function(str) {
        var reg = new RegExp("^" + str);
        return reg.test(this);
    };

    String.prototype.endWith = function(str) {
        var reg = new RegExp(str + "$");
        return reg.test(this);
    };

    String.prototype.isNullOrWhitespace = function() {
        return (this.length === 0 || this.replace(/\s+?/).length === 0);
    };

    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/\{(\d+)\}/g,
            function(m, i) {
                return args[i];
            });
    };
    
    

    Array.prototype.indexOfX = function(exp) {
        if (!(typeof exp === "function")) return undefined;

        var targetIndex = undefined;        

        this.forEach(function(item, index) {
            if (exp(item) && !targetIndex) targetIndex = index;
        });

        return targetIndex;
    };

    Array.prototype.findFromArrayBy = function (exp) {
        if (!(typeof exp === "function")) return undefined;
        
        var target = undefined;
        
        this.forEach(function (item, index) {
            if (exp(item) && !target) target = item;
        });
        
        return target;
    };
})();