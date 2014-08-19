function flotDate( bval) {
    this.d = new Date() ;
    this.getDate = function() {
        return this.d.getDate() ;
    }
    this.getDay = function() {
        return this.d.getDay() ;
    }
    this.getFullYear = function() {
        return this.d.getFullYear() ;
    }
    this.getHours = function() {
        return this.d.getHours() ;
    }
    this.getMilliseconds = function() {
        return this.d.getMilliseconds() ;
    }
    this.getMinutes = function() {
        return this.d.getMinutes() ;
    }
    this.getMonth = function() {
        return this.d.getMonth() ;
    }
    this.getSeconds = function() {
        return this.d.getSeconds() ; 
    }
    this.setDate = function() {
        return this.d.setDate.apply( this.d, arguments) ;
    }
    this.setFullYear = function() {
        return this.d.setFullYear.apply( this.d, arguments) ;
    }
    this.setHours = function() {
        return this.d.setHours.apply( this.d, arguments) ;
    }
    this.setMilliseconds = function() {
        return this.d.setMilliseconds.apply( this.d, arguments) ;
    }
    this.setMinutes = function() {
        return this.d.setMinutes.apply( this.d, arguments) ;
    }
    this.setMonth = function() {
        return this.d.setMonth.apply( this.d, arguments) ;
    }
    this.setSeconds = function() {
        return this.d.setSeconds.apply( this.d, arguments) ;
    }
    this.toString = function() {
        return this.d.toString.apply( this.d, arguments) ;
    }
    this.getTime = function() {
        return this.d.getTime() ;
    }
    this.getTimeZoneOffset = function() {
        return this.d.getTimeZoneOffset() ;
    }
    this.getYear = function() {
        return this.d.getYear() ;
    }
    this.setTime = function() {
        return this.d.setTime.apply( this.d, arguments) ;
    }
    this.setYear = function() {
        return this.d.setYear.apply( this.d, arguments) ;
    }
    this.toDateString = function() {
        return this.d.toDateString.apply( this.d, arguments) ;
    }
    this.toGMTString = function() {
        return this.d.toGMTString.apply( this.d, arguments) ;
    }
    this.toLocaleDateString = function() {
        return this.d.toLocaleDateString.apply( this.d, arguments) ;
    }
    this.toLocaleString = function() {
        return this.d.toLocaleString.apply( this.d, arguments) ;
    }
    this.toLocaleTimeString = function() {
        return this.d.toLocaleTimeString.apply( this.d, arguments) ;
    }
    this.toTimeString = function() {
        return this.d.toTimeString.apply( this.d, arguments) ;
    }
    this.valueOf = function() {
        return this.d.valueOf() ;
    }
    this.parse = function() {
        return Date.parse.apply( this.d, arguments) ;
    }
    this.UTC = function() {
        return Date.UTC.apply( this.d, arguments) ;
    }
    if (bval) {
        this.getUTCDate = this.d.getDate ;
        this.getUTCDay = this.d.getDay ;        
        this.getUTCFullYear = this.d.getFullYear ;
        this.getUTCHours = this.d.getHours ;        
        this.getUTCMilliseconds = this.d.getMilliseconds ;        
        this.getUTCMinutes = this.d.getMinutes ;
        this.getUTCMonth = this.d.getMonth ;
        this.getUTCSeconds = this.d.getSeconds ;        
        this.setUTCDate = this.d.setDate ;
        this.setUTCFullYear = this.d.setFullYear ;
        this.setUTCHours = this.d.setHours ;
        this.setUTCMilliseconds = this.d.setMilliseconds ;
        this.setUTCMinutes = this.d.setMinutes ;
        this.setUTCMonth = this.d.setMonth ;
        this.setUTCSeconds = this.d.setSeconds ;
        
        this.toUTCString = this.toString ; //This line is different from others because it is re-using the assignment outside the if
    } else {
        this.getUTCDate = function() {
            return this.d.getUTCDate() ;
        }
        this.getUTCDay = function() {
            return this.d.getUTCDay() ;
        }
        this.getUTCFullYear = function() {
            return this.d.getUTCFullYear() ;
        }
        this.getUTCHours = function() {
            return this.d.getUTCHours() ;
        }
        this.getUTCMilliseconds = function() {
            return this.d.getUTCMilliseconds() ;
        }
        this.getUTCMinutes = function() {
            return this.d.getUTCMinutes() ;
        }
        this.getUTCMonth = function() {
            return this.d.getUTCMonth() ;
        }
        this.getUTCSeconds = function() {
            return this.d.getUTCSeconds() ;
        }
        this.setUTCDate = function() {
            return this.d.setUTCDate.apply( this.d, arguments) ;
        }
        this.setUTCFullYear = function() {
            return this.d.setUTCFullYear.apply( this.d, arguments) ;
        }
        this.setUTCHours = function() {
            return this.d.setUTCHours.apply( this.d, arguments) ;
        }
        this.setUTCMilliseconds = function() {
            return this.d.setUTCMilliseconds.apply( this.d, arguments) ;
        }
        this.setUTCMinutes = function() {
            return this.d.setUTCMinutes.apply( this.d, arguments) ;
        }
        this.setUTCMonth = function() {
            return this.d.setUTCMonth.apply( this.d, arguments) ;
        }
        this.setUTCSeconds = function() {
            return this.d.setUTCSeconds.apply( this.d, arguments) ;
        }
        this.toUTCString = function() {
            return this.d.toUTCString.apply( this.d, arguments) ;
        }
    }
}
