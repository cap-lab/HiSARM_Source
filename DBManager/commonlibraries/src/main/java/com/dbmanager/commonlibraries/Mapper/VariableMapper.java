package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.variable.Candidate;
import com.dbmanager.datastructure.variable.EnumCandidate;
import com.dbmanager.datastructure.variable.FloatCandidate;
import com.dbmanager.datastructure.variable.IntegerCandidate;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.dbmanager.datastructure.variable.Variable;

public class VariableMapper {
    private static IntegerCandidate makeIntegerCandidate(Document document) {
        IntegerCandidate candidate = new IntegerCandidate();

        candidate.setMax(document.getInteger("max"));
        candidate.setMin(document.getInteger("min"));

        return candidate;
    }

    private static FloatCandidate makeFloatCandidate(Document document) {
        FloatCandidate candidate = new FloatCandidate();

        candidate.setMax(document.getInteger("max"));
        candidate.setMin(document.getInteger("min"));

        return candidate;
    }

    private static EnumCandidate makeEnumCandidate(Document document) {
        EnumCandidate candidate = new EnumCandidate();

        document.getList("candidate", String.class).forEach(c -> candidate.addCandidate(c));

        return candidate;
    }

    private static Candidate makeCandidate(PrimitiveType type, Document document) {
        Candidate candidate = null;
        switch (type) {
            case INT16:
            case INT32:
            case INT64:
                candidate = makeIntegerCandidate(document);
                break;
            case FLOAT:
                candidate = makeFloatCandidate(document);
                break;
            case ENUM:
                candidate = makeEnumCandidate(document);
                break;
        }
        return candidate;
    }

    public static Variable mapToVariable(Bson bson) {
        Variable variable = new Variable();
        Document document = (Document) bson;

        try {
            variable.setName(document.getString("Name"));
            variable.setType(PrimitiveType.fromString(document.getString("Type")));
            variable.setSize(document.getInteger("Size"));
            variable.setCount(document.getInteger("Count"));
            variable.setCandidate(
                    makeCandidate(variable.getType(), (Document) document.get("CandidateValue")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return variable;
    }
}
