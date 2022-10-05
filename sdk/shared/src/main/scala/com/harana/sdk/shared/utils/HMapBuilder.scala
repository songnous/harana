package com.harana.sdk.shared.utils

class HMapBuilder[R[_, _]] {

  def apply
  [K0, V0]
  (e0: (K0, V0))
  (implicit ev0: R[K0, V0])
  = new HMap[R](Map(e0))

  def apply
  [K0, V0, K1, V1]
  (e0: (K0, V0), e1: (K1, V1))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1])
  = new HMap[R](Map(e0, e1))

  def apply
  [K0, V0, K1, V1, K2, V2]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2])
  = new HMap[R](Map(e0, e1, e2))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3])
  = new HMap[R](Map(e0, e1, e2, e3))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4])
  = new HMap[R](Map(e0, e1, e2, e3, e4))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16, K17, V17]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16), e17: (K17, V17))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16], ev17: R[K17, V17])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16, K17, V17, K18, V18]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16), e17: (K17, V17), e18: (K18, V18))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16], ev17: R[K17, V17], ev18: R[K18, V18])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16, K17, V17, K18, V18, K19, V19]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16), e17: (K17, V17), e18: (K18, V18), e19: (K19, V19))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16], ev17: R[K17, V17], ev18: R[K18, V18], ev19: R[K19, V19])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16, K17, V17, K18, V18, K19, V19, K20, V20]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16), e17: (K17, V17), e18: (K18, V18), e19: (K19, V19), e20: (K20, V20))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16], ev17: R[K17, V17], ev18: R[K18, V18], ev19: R[K19, V19], ev20: R[K20, V20])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20))

  def apply
  [K0, V0, K1, V1, K2, V2, K3, V3, K4, V4, K5, V5, K6, V6, K7, V7, K8, V8, K9, V9, K10, V10, K11, V11, K12, V12, K13, V13, K14, V14, K15, V15, K16, V16, K17, V17, K18, V18, K19, V19, K20, V20, K21, V21]
  (e0: (K0, V0), e1: (K1, V1), e2: (K2, V2), e3: (K3, V3), e4: (K4, V4), e5: (K5, V5), e6: (K6, V6), e7: (K7, V7), e8: (K8, V8), e9: (K9, V9), e10: (K10, V10), e11: (K11, V11), e12: (K12, V12), e13: (K13, V13), e14: (K14, V14), e15: (K15, V15), e16: (K16, V16), e17: (K17, V17), e18: (K18, V18), e19: (K19, V19), e20: (K20, V20), e21: (K21, V21))
  (implicit ev0: R[K0, V0], ev1: R[K1, V1], ev2: R[K2, V2], ev3: R[K3, V3], ev4: R[K4, V4], ev5: R[K5, V5], ev6: R[K6, V6], ev7: R[K7, V7], ev8: R[K8, V8], ev9: R[K9, V9], ev10: R[K10, V10], ev11: R[K11, V11], ev12: R[K12, V12], ev13: R[K13, V13], ev14: R[K14, V14], ev15: R[K15, V15], ev16: R[K16, V16], ev17: R[K17, V17], ev18: R[K18, V18], ev19: R[K19, V19], ev20: R[K20, V20], ev21: R[K21, V21])
  = new HMap[R](Map(e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21))
}