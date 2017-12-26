


/*
 *
 * Copyright (c) 2011 The Android Open Source Project
 * Copyright (c) 2016, alipay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
// Created by xiawanli on 2017/12/22.
//

#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <string>
#include <memory>
#include <sys/mman.h>

#include <fcntl.h>
#include <dlfcn.h>

#include <stdint.h>    /* C99 */


#ifndef ANDFIXPROJECT_ART_7_0_H
#define ANDFIXPROJECT_ART_7_0_H

#endif //ANDFIXPROJECT_ART_7_0_H

namespace art {
    namespace mirror {
        class ArtMethod {
        public:

            // Field order required by test "ValidateFieldOrderOfJavaCppUnionClasses".
            // The class we are a part of.
            uint32_t declaring_class_;
            // Access flags; low 16 bits are defined by spec.
            uint32_t access_flags_;
            /* Dex file fields. The defining dex file is available via declaring_class_->dex_cache_ */
            // Offset to the CodeItem.
            uint32_t dex_code_item_offset_;
            // Index into method_ids of the dex file associated with this method.
            uint32_t dex_method_index_;
            /* End of dex file fields. */
            // Entry within a dispatch table for this method. For static/direct methods the index is into
            // the declaringClass.directMethods, for virtual methods the vtable and for interface methods the
            // ifTable.
            uint16_t method_index_;

            // The hotness we measure for this method. Incremented by the interpreter. Not atomic, as we allow
            // missing increments: if the method is hot, we will see it eventually.
            uint16_t hotness_count_;
            // Fake padding field gets inserted here.
            // Must be the last fields in the method.
            // PACKED(4) is necessary for the correctness of
            // RoundUp(OFFSETOF_MEMBER(ArtMethod, ptr_sized_fields_), pointer_size).
            struct PtrSizedFields {
                // Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
                ArtMethod** dex_cache_resolved_methods_;

                // Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
                void* dex_cache_resolved_types_;

                // Pointer to JNI function registered to this method, or a function to resolve the JNI function,
                // or the profiling data for non-native methods, or an ImtConflictTable.
                void* entry_point_from_jni_;

                // Method dispatch from quick compiled code invokes this pointer which may cause bridging into
                // the interpreter.
                void* entry_point_from_quick_compiled_code_;
            } ptr_sized_fields_;

        };

    }
}