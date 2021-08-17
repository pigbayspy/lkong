package io.pig.lkong.http.data.resp

import io.pig.lkong.http.data.resp.data.PostRespThreadData
import io.pig.lkong.http.data.resp.data.PostRespPostData

class ThreadPostResp(val thread: PostRespThreadData, val posts: List<PostRespPostData>)