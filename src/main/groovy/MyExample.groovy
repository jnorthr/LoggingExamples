import groovy.util.logging.Slf4j

@Slf4j
class MyExample {

    MyExample() {
        log.info 'Instantiated MyExample'
    }

    def status(int i) {
        log.debug 'Still alive... at interation '+i;
    }

    def warn() {
        log.warn '-- this is a warning ';
    }

    static main(args) {
        def ex = new MyExample()
		def ct = 0
        while(true) {
          ex.status(++ct)
          sleep(10000) // Sleep 30 seconds
		  if (ct<4) ex.warn();
        }
    }
}

