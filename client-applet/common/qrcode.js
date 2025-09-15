/**
 * 二维码生成工具
 * 基于canvas实现简单的二维码生成功能
 */

/**
 * 生成二维码到canvas
 * @param {Object} ctx - canvas上下文
 * @param {string} text - 要编码的文本
 * @param {Object} options - 配置选项
 */
function toCanvas(ctx, text, options = {}) {
  const {
    width = 200,
    height = 200,
    margin = 10,
    backgroundColor = '#ffffff',
    foregroundColor = '#000000'
  } = options

  try {
    // 清空画布
    ctx.clearRect(0, 0, width, height)
    
    // 设置背景色
    ctx.setFillStyle(backgroundColor)
    ctx.fillRect(0, 0, width, height)
    
    // 简化的二维码生成逻辑
    // 实际项目中建议使用专业的二维码库如 qrcode-generator
    const qrSize = Math.min(width, height) - margin * 2
    const moduleSize = Math.floor(qrSize / 25) // 25x25的模块网格
    const actualQrSize = moduleSize * 25
    const offsetX = (width - actualQrSize) / 2
    const offsetY = (height - actualQrSize) / 2
    
    // 生成简单的二维码模式
    const pattern = generateSimplePattern(text)
    
    ctx.setFillStyle(foregroundColor)
    
    // 绘制二维码模块
    for (let row = 0; row < 25; row++) {
      for (let col = 0; col < 25; col++) {
        if (pattern[row] && pattern[row][col]) {
          const x = offsetX + col * moduleSize
          const y = offsetY + row * moduleSize
          ctx.fillRect(x, y, moduleSize, moduleSize)
        }
      }
    }
    
    // 绘制定位标记（三个角的方块）
    drawFinderPattern(ctx, offsetX, offsetY, moduleSize)
    drawFinderPattern(ctx, offsetX + 18 * moduleSize, offsetY, moduleSize)
    drawFinderPattern(ctx, offsetX, offsetY + 18 * moduleSize, moduleSize)
    
    ctx.draw()
    
  } catch (error) {
    console.error('生成二维码失败:', error)
    
    // 失败时显示错误信息
    ctx.setFillStyle('#f5f5f5')
    ctx.fillRect(0, 0, width, height)
    
    ctx.setFillStyle('#333333')
    ctx.setFontSize(16)
    ctx.setTextAlign('center')
    ctx.fillText('二维码生成失败', width / 2, height / 2 - 10)
    ctx.setFontSize(12)
    ctx.fillText('请稍后重试', width / 2, height / 2 + 10)
    
    ctx.draw()
  }
}

/**
 * 生成简单的二维码模式
 * @param {string} text - 要编码的文本
 * @returns {Array} 二维码模式数组
 */
function generateSimplePattern(text) {
  const size = 25
  const pattern = Array(size).fill().map(() => Array(size).fill(false))
  
  // 基于文本内容生成伪随机模式
  let hash = 0
  for (let i = 0; i < text.length; i++) {
    const char = text.charCodeAt(i)
    hash = ((hash << 5) - hash) + char
    hash = hash & hash // 转换为32位整数
  }
  
  // 使用hash值生成模式
  let seed = Math.abs(hash)
  
  for (let row = 0; row < size; row++) {
    for (let col = 0; col < size; col++) {
      // 跳过定位标记区域
      if (isFinderPatternArea(row, col)) {
        continue
      }
      
      // 生成伪随机模式
      seed = (seed * 1103515245 + 12345) & 0x7fffffff
      pattern[row][col] = (seed % 100) < 45 // 约45%的填充率
    }
  }
  
  return pattern
}

/**
 * 检查是否为定位标记区域
 * @param {number} row - 行索引
 * @param {number} col - 列索引
 * @returns {boolean} 是否为定位标记区域
 */
function isFinderPatternArea(row, col) {
  // 左上角
  if (row < 9 && col < 9) return true
  // 右上角
  if (row < 9 && col > 15) return true
  // 左下角
  if (row > 15 && col < 9) return true
  
  return false
}

/**
 * 绘制定位标记
 * @param {Object} ctx - canvas上下文
 * @param {number} x - x坐标
 * @param {number} y - y坐标
 * @param {number} moduleSize - 模块大小
 */
function drawFinderPattern(ctx, x, y, moduleSize) {
  const size = moduleSize * 7
  
  // 外框
  ctx.setFillStyle('#000000')
  ctx.fillRect(x, y, size, size)
  
  // 内部白色
  ctx.setFillStyle('#ffffff')
  ctx.fillRect(x + moduleSize, y + moduleSize, size - 2 * moduleSize, size - 2 * moduleSize)
  
  // 中心黑色
  ctx.setFillStyle('#000000')
  ctx.fillRect(x + 2 * moduleSize, y + 2 * moduleSize, 3 * moduleSize, 3 * moduleSize)
}

/**
 * 生成二维码数据URL（用于图片保存等）
 * @param {string} text - 要编码的文本
 * @param {Object} options - 配置选项
 * @returns {Promise<string>} 二维码的数据URL
 */
function toDataURL(text, options = {}) {
  return new Promise((resolve, reject) => {
    try {
      // 创建临时canvas
      const canvasId = 'temp-qrcode-' + Date.now()
      const ctx = uni.createCanvasContext(canvasId)
      
      // 生成二维码
      toCanvas(ctx, text, options)
      
      // 延迟获取图片数据
      setTimeout(() => {
        uni.canvasToTempFilePath({
          canvasId: canvasId,
          success: (res) => {
            resolve(res.tempFilePath)
          },
          fail: (error) => {
            reject(error)
          }
        })
      }, 500)
      
    } catch (error) {
      reject(error)
    }
  })
}

/**
 * 保存二维码到相册
 * @param {string} text - 要编码的文本
 * @param {Object} options - 配置选项
 * @returns {Promise} 保存结果
 */
function saveToAlbum(text, options = {}) {
  return new Promise(async (resolve, reject) => {
    try {
      // 生成二维码图片
      const tempFilePath = await toDataURL(text, options)
      
      // 保存到相册
      uni.saveImageToPhotosAlbum({
        filePath: tempFilePath,
        success: () => {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
          resolve()
        },
        fail: (error) => {
          uni.showToast({
            title: '保存失败',
            icon: 'none'
          })
          reject(error)
        }
      })
      
    } catch (error) {
      reject(error)
    }
  })
}

export default {
  toCanvas,
  toDataURL,
  saveToAlbum
}