package br.com.zup.apivacina.shared.util

enum class NomeVacina {
    CORONAVAC {
        override fun validar(): Boolean {
            return true;
        }
    },
    PFIZER {
        override fun validar(): Boolean {
            return true;
        }
    },
    JANSSEN {
        override fun validar(): Boolean {
            return true;
        }
    },
    ASTRAZENECA {
        override fun validar(): Boolean {
            return true;
        }
    },
    ;

    abstract fun validar(): Boolean
}
