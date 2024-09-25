package com.green.security.validation;

import com.green.security.validation.ValidationGroups.*;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankGroup.class, PatternGroup.class, AssertTrueGroup.class})
public interface ValidationSequence {

}
