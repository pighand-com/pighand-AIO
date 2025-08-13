import constant from './constant'

interface ShareParams {
  title?: string
  subtitle?: string
  path?: string
  params?: Record<string, any>
  imageUrl?: string
}

interface ShareInfo {
  title: string
  path: string
  imageUrl: string
}

/**
 * 分享工具类
 * @param options 分享参数
 * @returns 分享信息JSON
 */
export function createShareInfo(options: ShareParams = {}): ShareInfo {
  const { title, subtitle, path, params, imageUrl } = options
  
  // 处理标题
  let shareTitle = constant.share.title
  if (title) {
    // 传入title则覆盖默认title
    shareTitle = title
  } else if (subtitle) {
    // 传入subtitle则拼接 title - subtitle
    shareTitle = `${constant.share.title} - ${subtitle}`
  }
  
  // 处理路径
  let sharePath = path
  if (!sharePath) {
    // 使用getCurrentPages获取当前页面路径
    const pages = getCurrentPages()
    if (pages.length > 0) {
      const currentPage = pages[pages.length - 1]
      sharePath = `/${currentPage.route}`
    } else {
      sharePath = '/'
    }
  }

  console.log(sharePath)
  
  // 如果有params参数，追加到路径中
  if (params && Object.keys(params).length > 0) {
    const queryString = Object.entries(params)
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&')
    sharePath += sharePath.includes('?') ? `&${queryString}` : `?${queryString}`
  }
  
  // 处理图片URL
  const shareImageUrl = imageUrl || constant.share.imageUrl
  
  return {
    title: shareTitle,
    path: sharePath,
    imageUrl: shareImageUrl
  }
}

/**
 * 默认导出分享工具
 */
export default {
  createShareInfo
}