package nl.of.catchplus

class MetaRecordKey extends BaseDomain
{

    String label

    static constraints = {
        label(nullable: true, blank: false, unique: true)
    }

    public String toString()
    {
        label
    }
}
